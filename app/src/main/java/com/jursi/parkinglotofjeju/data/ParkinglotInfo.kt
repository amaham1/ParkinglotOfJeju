package com.jursi.parkinglotofjeju.data

data class ParkinglotInfoItem(
    var ISTL_LCTN_ADDR: String? = null,
    var UPDT_DT: String? = null,
    var GNRL_RMND_PRZN_NUM: String? = null,
    var HNDC_RMND_PRZN_NUM: String? = null,
    var LGVH_RMND_PRZN_NUM: String? = null,
    var EMVH_RMND_PRZN_NUM: String? = null,
    var HVVH_RMND_PRZN_NUM: String? = null,
    var TV_updatedTime: String? = null

) {

    //주차장 장소
    fun saveISTL_LCTN_ADDR(ISTL_LCTN_ADDR: String?) {
        this.ISTL_LCTN_ADDR = ISTL_LCTN_ADDR
    }

    //주차장 정보 갱신 시간
    fun saveUPDT_DT(UPDT_DT: String?) {
        this.UPDT_DT = UPDT_DT
    }

    // 일반 주차 잔여개수
    fun saveGNRL_RMND_PRZN_NUM(GNRL_RMND_PRZN_NUM: String?) {
        this.GNRL_RMND_PRZN_NUM = GNRL_RMND_PRZN_NUM
    }

    // 장애인 주차 잔여개수
    fun saveHNDC_RMND_PRZN_NUM(HNDC_RMND_PRZN_NUM: String?) {
        this.HNDC_RMND_PRZN_NUM = HNDC_RMND_PRZN_NUM
    }

     // 경차 주차 잔여개수
    fun saveLGVH_RMND_PRZN_NUM(LGVH_RMND_PRZN_NUM: String?) {
        this.LGVH_RMND_PRZN_NUM = LGVH_RMND_PRZN_NUM
    }

     // 긴급차량 주차 잔여개수
    fun saveEMVH_RMND_PRZN_NUM(EMVH_RMND_PRZN_NUM: String?) {
        this.EMVH_RMND_PRZN_NUM = EMVH_RMND_PRZN_NUM
    }

     // 대형 잔여 주차구역 개수
    fun saveHVVH_RMND_PRZN_NUM(HVVH_RMND_PRZN_NUM: String?) {
        this.HVVH_RMND_PRZN_NUM = HVVH_RMND_PRZN_NUM
    }


    fun saveTV_updatedTime(TV_updatedTime: String?) {
        this.TV_updatedTime = TV_updatedTime
    }
}