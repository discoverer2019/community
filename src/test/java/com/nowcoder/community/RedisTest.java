package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings() {
        String redisKey = "test:spring";
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set(redisKey, 1);
        System.out.println(ops.get(redisKey));
        System.out.println(ops.increment(redisKey));
        System.out.println(ops.decrement(redisKey));
    }

    @Test
    public void testHashes() {
        String redisKey = "test:count";

        HashOperations ops = redisTemplate.opsForHash();
        ops.put(redisKey, "id", 1);
        ops.put(redisKey, "username", "username");

        System.out.println(ops.get(redisKey, "id"));
        System.out.println(ops.get(redisKey, "username"));
    }

    @Test
    public void testLists() {
        String redisKey = "test:ids";

        ListOperations ops = redisTemplate.opsForList();
        ops.leftPush(redisKey, 1);
        ops.leftPush(redisKey, 2);
        ops.leftPush(redisKey, 3);

        System.out.println(ops.size(redisKey));
        System.out.println(ops.index(redisKey, 0));
        System.out.println(ops.range(redisKey, 0, 2));

        System.out.println(ops.leftPop(redisKey));
        System.out.println(ops.rightPop(redisKey));
    }

    @Test
    public void testSets() {
        String redisKey = "test:teachers";
        SetOperations ops = redisTemplate.opsForSet();

        ops.add(redisKey, "刘备", "关羽", "张飞", "赵云", "诸葛亮");

        System.out.println(ops.size(redisKey));
        System.out.println(ops.pop(redisKey));
        System.out.println(ops.members(redisKey));

    }

    @Test
    public void testSortedSets() {
        String redisKey = "test:students";

        ZSetOperations ops = redisTemplate.opsForZSet();

        ops.add(redisKey, "唐僧", 80);
        ops.add(redisKey, "悟空", 90);
        ops.add(redisKey, "八戒", 50);
        ops.add(redisKey, "沙僧", 70);

        System.out.println(ops.zCard(redisKey));
        System.out.println(ops.score(redisKey, "八戒"));
        System.out.println(ops.reverseRank(redisKey, "八戒")); // 统计八戒排名
        System.out.println(ops.reverseRange(redisKey, 0, 2)); // 取排名范围内的key
    }

    @Test
    public void testKeys() {
        redisTemplate.delete("test:user");

        System.out.println(redisTemplate.hasKey("test:user"));
        redisTemplate.expire("test:students", 10, TimeUnit.SECONDS);
    }

    // 多次访问同一个key
    @Test
    public void testBoundOperations() {
        String redisKey = "test:count";
        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);


        // 再进行方法调用的时候不需要传入key了
        System.out.println(operations.increment());;
        System.out.println(operations.decrement());;
    }

    @Test
    public void testTransactional() {
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:tx";

                operations.multi();

                operations.opsForSet().add(redisKey, "zhangsan");
                operations.opsForSet().add(redisKey, "lisi");
                operations.opsForSet().add(redisKey, "wangwu");

                // redis管理事务的时候，中间一定不要做查询，因为他事务中的语句都是

                // 在开启事务和提交事务之间，事把命令放到队列中，不会立刻执行
                System.out.println(operations.opsForSet().members(redisKey));
                return operations.exec();

            }
        });
        System.out.println(obj);
    }










}
