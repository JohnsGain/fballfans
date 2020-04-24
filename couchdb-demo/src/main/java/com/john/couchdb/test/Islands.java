package com.john.couchdb.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-19
 * @since jdk1.8
 */
public class Islands {

    public static int[][] v;

    public int[][] dir = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};//方向

    public static int count;

//    public void DFS(int i, int j) {
//        if (i < 0 || i >= v.length || j < 0 || j >= v[i].length || v[i][j] != 1)
//            return;
//        v[i][j] = -1;//染色，或者使用visit禁忌表
//        DFS(i + 1, j);
//        DFS(i - 1, j);
//        DFS(i, j + 1);
//        DFS(i, j - 1);
//    }

    void BFS(int i, int j) {
        LinkedList<IntPair> list = new LinkedList<>();
        list.add(new IntPair(i, j));
        v[i][j] = -1;//染色，或者使用visit禁忌表
        int d = dir.length;
        while (!list.isEmpty()) {
            IntPair p = list.pop();
            for (int k = 0; k < d; ++k) {
                int x = p.i + dir[k][0], y = p.j + dir[k][1];
                if (x < 0 || x >= v.length || y < 0 || y >= v[0].length)
                    continue;
                if (v[x][y] == 1) {
                    list.add(new IntPair(x, y));
                    v[x][y] = -1;//一定是在入栈的时候进行染色
                }
            }
        }
    }

    @Test
    public void contextLoads1() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int x = 6;
        int y = 8;
        v = new int[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                v[i][j] = random.nextInt(2);
            }
        }
        for (int[] ints : v) {
            for (int anInt : ints) {
                System.out.print(anInt + "\t");
            }
            System.out.println();
        }

        for (int i = 0; i < v.length; ++i) {
            for (int j = 0; j < v[i].length; ++j) {
                if (v[i][j] == 1) {
                    BFS(i, j);
                    //DFS(i, j);//切记，一般内存超出的问题就是数量太大，导致DFS递归的内存栈溢出，应换用BFS
                    count += 1;
                }
            }
        }
        System.out.println(count);
    }

    @Data
    @AllArgsConstructor
    public static class IntPair {

        public int i;
        public int j;
    }
}
