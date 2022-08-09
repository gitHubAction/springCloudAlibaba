package com.seven.gateway;

import com.alibaba.cloud.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangshihao01
 * @version 1.0
 * @description
 * @date 2022/7/21 11:05
 */
public class FallingSquares {

    public List<Integer> fallingSquares(int[][] positions) {
        List<Integer> res = new ArrayList<>();
        Node root = null;
        int maxHigh = 0;
        for(int[] pos : positions){
            int l = pos[0],r = pos[0] + pos[1], high = pos[1];
            // 查找[l,r]区间内最大高度
            int regionHigh = query(root, l, r);
            // 更新[l,r]区间内的高度
            root = update(root, l, r, regionHigh+high);
            maxHigh = Math.max(maxHigh, regionHigh+ high);
            res.add(maxHigh);
        }
        return res;
    }

    private Node update(Node root, int l, int r, int high) {
        if(root == null)return new Node(l, r, high, r);
        if(l <= root.l){
            root.left = update(root.left, l, r, high);
        }else{
            root.right = update(root.right, l, r, high);
        }
        root.maxR = Math.max(r, root.maxR);
        return root;
    }

    private int query(Node root, int l, int r) {
        // 如果根节点为null（空树）或者 要查找的左边界大于树的最大右边界 直接返回0
        if(root == null || root.maxR <= l)return 0;
        // 是否有交集
        int maxHigh = 0;
        if(l < root.r && r > root.l){
            maxHigh = root.h;
        }
        maxHigh = Math.max(maxHigh, query(root.left, l, r));
        if(r > root.l){
            maxHigh = Math.max(maxHigh, query(root.right, l, r));
        }
        return maxHigh;
    }


    class Node{
        int l,r,maxR, h;
        Node left, right;
        public Node(int l, int r, int h, int maxR){
            this.l = l;
            this.r = r;
            this.h = h;
            this.maxR = maxR;
        }
    }
}
