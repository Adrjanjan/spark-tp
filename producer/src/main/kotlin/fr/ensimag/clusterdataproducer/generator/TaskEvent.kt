package fr.ensimag.clusterdataproducer.generator

class TaskEvent(values: List<String>) {
    val time: Long //1,time,INTEGER,YES
    val missingInfo: Int? //2,missing info,INTEGER,NO
    val jobId: Long //3,job ID,INTEGER,YES
    val taskIndex: Long //4,task index,INTEGER,YES
    val machineId: Int? //5,machine ID,INTEGER,NO
    val eventType: Int //6,event type,INTEGER,YES
    val user: String? //7,user,STRING_HASH,NO
    val schedulingClass: Int? //8,scheduling class,INTEGER,NO
    val priority: Int //9,priority,INTEGER,YES
    val cpuRequest: Float? //10,CPU request,FLOAT,NO
    val memoryRequest: Float? //11,memory request,FLOAT,NO
    val diskSpaceRequest: Float? //12,disk space request,FLOAT,NO
    val differentMachinesRestriction: Boolean?//13,different machines restriction,BOOLEAN,NO

    init {
        val baseTime = 1304269200 - 600
//        val baseTime = 1640390400 - 600
        time = baseTime + values[0].toLong().floorDiv(1000000)
        missingInfo = values[1].toIntOrNull()
        jobId = values[2].toLong()
        taskIndex = values[3].toLong()
        machineId = values[4].toIntOrNull()
        eventType = values[5].toInt()
        user = values[6]
        schedulingClass = values[7].toIntOrNull()
        priority = values[8].toInt()
        cpuRequest = values[9].toFloatOrNull()
        memoryRequest = values[10].toFloatOrNull()
        diskSpaceRequest = values[11].toFloatOrNull()
        differentMachinesRestriction = values[12].toBooleanStrictOrNull()
    }
}
