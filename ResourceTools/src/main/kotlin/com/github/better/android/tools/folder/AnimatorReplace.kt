package com.github.better.android.tools.folder

import com.github.better.android.tools.ResToolsConfig
import com.github.better.android.tools.base.BaseFolderResReplace
import com.github.better.android.tools.debug
import java.io.FilenameFilter
import java.util.*

/**
 * Animator
 * Created by better On 2019-07-07.
 */
class AnimatorReplace(config: ResToolsConfig) : BaseFolderResReplace(config) {

    /**
     * 资源名称
     */
    private val RES_TYPE_NAME = "animator"
    /**
     * 需要过滤掉 animator
     */
    private val DIR_FILTER = FilenameFilter { file, s -> file.isDirectory && s.startsWith(RES_TYPE_NAME) }

    override val resTypeName: String = RES_TYPE_NAME
    override val javaRegex: String = """(R(\s*?)\.(\s*?)animator(\s*?)\.(\s*?))(\w+)"""
    override val xmlRegex: String = """(@animator/)(\w+)"""

    /**
     * 过滤掉 animator 的文件夹
     * 获取 res/anim/下的所有文件，并将文件转换成集合
     * 如：[res/anim/better.xml,res/anim/cz.xml] 返回 [better, cz]
     */
    override fun getResNameSet(): Set<String> {
        return resDir.listFiles(DIR_FILTER)?.map { it.name.substringBeforeLast(".") }?.toSet()
                ?: Collections.emptySet()
    }

    override fun replaceSrc(resNameSet: Set<String>, regex: String) {
        debug("---------- $RES_TYPE_NAME ----- replace source folder start...")
        replaceSrcDir(srcDir, resNameSet, javaRegex)
        debug("---------- $RES_TYPE_NAME ----- replace source folder end")
    }

    override fun replaceRes(resNameSet: Set<String>, regex: String) {
        // 1.替换文件内容
        debug("---------- $RES_TYPE_NAME ----- replace res folder start...")
        replaceResDir(resDir, resNameSet, xmlRegex, null)
        debug("---------- $RES_TYPE_NAME ----- replace res folder end")

        // 2.修改文件名
        debug("---------- $RES_TYPE_NAME ----- rename start...")
        renameFile(resDir, resNameSet, DIR_FILTER, RES_TYPE_NAME)
        debug("---------- $RES_TYPE_NAME ----- rename end")
    }

}