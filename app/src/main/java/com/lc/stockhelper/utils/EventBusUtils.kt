package com.lc.stockhelper.utils

import org.greenrobot.eventbus.EventBus

/**
 * Created by KID on 2019-10-08.
 */
class EventBusUtils {

    companion object{

        val eventBus = EventBus.getDefault()

        fun post(any:Any){
            eventBus.post(any)
        }

        fun register(any: Any){
            eventBus.register(any)
        }

        fun unRegister(any: Any){
            eventBus.unregister(any)
        }

    }

}