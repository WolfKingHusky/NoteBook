package com.huang.notetool.util;


import java.util.Random;
import java.util.UUID;

/**
 * 生成随机数
 *
 * @author 黄先生
 * @version 1.0
 * @Content 此处说明类的解释
 * @CreateDate 2019-03-18 12:07:15
 * @History 序号   日期         修改内容          修改进度
 * * * * * * 1.0    2019-03-18   创建项目           完成
 * @LeftoverProblem 此处说明遗留问题和潜在的问题
 */
public class GenerateRandom {
    /**
     * Random object used by random method. This has to be not local to the
     * random method so as to not return the same value in the same millisecond.
     */
    private static final Random RANDOM = new Random();

    /**
     * 生成int型的随机数
     *
     * @return int型的随机数
     */
    public static int randomInt() {
        int min = 1;
        int max = 655340000;
        return nextInt(min, max);
    }

    /**
     * <p>
     * Returns a random integer within the specified range.
     * </p>
     *
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endExclusive   the upper bound (not included)
     * @return the random integer
     * @throws IllegalArgumentException if {@code startInclusive > endExclusive} or if
     *                                  {@code startInclusive} is negative
     */
    public static int nextInt(final int startInclusive, final int endExclusive) {
        Validate.isTrue(endExclusive >= startInclusive,
                "Start value must be smaller or equal to end value.");
        Validate.isTrue(startInclusive >= 0, "Both range values must be non-negative.");

        if (startInclusive == endExclusive) {
            return startInclusive;
        }

        return startInclusive + RANDOM.nextInt(endExclusive - startInclusive);
    }

    /**
     * 生成String型的随机数
     *
     * @return String型的随机数
     */
    public static String randomString() {
        return UUID.randomUUID().toString();
    }
}
