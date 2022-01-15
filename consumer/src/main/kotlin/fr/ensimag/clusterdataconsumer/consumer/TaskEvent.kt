package fr.ensimag.clusterdataconsumer.consumer

data class TaskEvent(
    val time: Long, //1,time,INTEGER,YES
    val missingInfo: Int?, //2,missing info,INTEGER,NO
    val jobId: Long, //3,job ID,INTEGER,YES
    val taskIndex: Long, //4,task index,INTEGER,YES
    val machineId: Int?, //5,machine ID,INTEGER,NO
    val eventType: Int, //6,event type,INTEGER,YES
    val user: String?, //7,user,STRING_HASH,NO
    val schedulingClass: Int?, //8,scheduling class,INTEGER,NO
    val priority: Int, //9,priority,INTEGER,YES
    val cpuRequest: Float?, //10,CPU request,FLOAT,NO
    val memoryRequest: Float?, //11,memory request,FLOAT,NO
    val diskSpaceRequest: Float?, //12,disk space request,FLOAT,NO
    val differentMachinesRestriction: Boolean? //13,different machines restriction,BOOLEAN,NO
)