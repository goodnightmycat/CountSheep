package com.example.countsheep

import kotlin.random.Random

/**
 * @author fanxiaoyang
 * date 2021/6/26
 * desc
 */
class RandomSheepSrc {
    companion object {

        fun getSheep(): Int {
            return sheepSrcArray[Random.nextInt(0, sheepSrcArray.size)]
        }

        val sheepSrcArray =
            arrayListOf(R.drawable.sheep_8a8a8a, R.drawable.sheep_bfbfbf, R.drawable.sheep_cdcdcd)
    }
}