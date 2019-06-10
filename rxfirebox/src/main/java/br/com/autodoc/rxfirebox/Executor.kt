package br.com.autodoc.rxfirebox

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.SECONDS

class Executor {

    companion object {
        fun executeThreadPoolExecutor(): ThreadPoolExecutor {
            var numCores = Runtime.getRuntime().availableProcessors()
            return ThreadPoolExecutor(numCores * 2, numCores * 2,
                    60L, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())
        }
    }

}


