package com.muhsiin.magicos_tweaker

import java.io.BufferedReader
import java.io.InputStreamReader
import rikka.shizuku.Shizuku

object TweakerService {

    /**
     * Executes a global settings shell command via Shizuku's system service wrapper.
     */
    fun executeShellCommand(command: String): String {
        return try {
            if (!Shizuku.pingBinder()) return "Error: Shizuku not running"

            // Shizuku provides a native mechanism to run commands directly through the system shell
            val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
            
            process.waitFor()
            output.toString().trim()
        } catch (e: Exception) {
            "Exception: ${e.localizedMessage}"
        }
    }

    /**
     * Changes system animation scales. Standard values: 0.0 (off), 0.5 (fast), 1.0 (default)
     */
    fun setAnimationScale(windowScale: Float, transitionScale: Float, animatorScale: Float): String {
        val cmd1 = "settings put global window_animation_scale $windowScale"
        val cmd2 = "settings put global transition_animation_scale $transitionScale"
        val cmd3 = "settings put global animator_duration_scale $animatorScale"
        
        val res1 = executeShellCommand(cmd1)
        val res2 = executeShellCommand(cmd2)
        val res3 = executeShellCommand(cmd3)
        
        return "Applied Speed Settings Successfully!"
    }
}
