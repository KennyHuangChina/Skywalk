package com.kjs.skywalk.communicationlibrary;

import java.util.ArrayList;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.*;

/**
 * Created by kenny on 2017/12/27.
 */

public class IApiArgs {
    public interface IArgsBase {
        boolean checkArgs();
        boolean isEqual(IArgsBase arg2);
    }

    public interface IArgsGetHouseDigestList extends IArgsBase {
        int                     getType();
        int                     getBeginPosi();
        int                     getFetchCnt();
        HouseFilterCondition    getFilters();
        ArrayList<Integer>      getSorts();
    }

    public static int   GET_HOUSE_DIG_LST_TYPE_BEGIN        = 0,
                        GET_HOUSE_DIG_LST_TYPE_ALL          = 0,
                        GET_HOUSE_DIG_LST_TYPE_RECOMMEND    = 1,
                        GET_HOUSE_DIG_LST_TYPE_DEDUCTED     = 2,
                        GET_HOUSE_DIG_LST_TYPE_NEW          = 3,
                        GET_HOUSE_DIG_LST_TYPE_END          = GET_HOUSE_DIG_LST_TYPE_NEW;

    public interface IArgsGetXPicLst extends IArgsBase {
        int getXId();
        int getSubType();   // picture sub-type, ref to PIC_TYPE_SUB_HOUSE_xxx
        int getSize();      // picture size, ref to PIC_SIZE_xxx
    }

    static public int   PIC_TYPE_MAJOR_User     = 100,
                        PIC_TYPE_MAJOR_House    = 200,
                        PIC_TYPE_MAJOR_Rental   = 300;

    static public int   PIC_TYPE_SUB_USER_IDCard        = 1,    // User's ID card
                        PIC_TYPE_SUB_USER_Headportrait  = 2;    // user's head-portrait

    // picture sub-type
    static public int   PIC_TYPE_SUB_HOUSE_BEGIN        = 0,    // all type
                        PIC_TYPE_SUB_HOUSE_FLOOR_PLAN   = 1,    // house plan
                        PIC_TYPE_SUB_HOUSE_FURNITURE    = 2,    // house furnitures
                        PIC_TYPE_SUB_HOUSE_APPLIANCE    = 3,    // house appliances
                        PIC_TYPE_SUB_HOUSE_OwnershipCert= 4,    // owernship certification
                        PIC_TYPE_SUB_HOUSE_RealMap      = 5,    // house real map
                        PIC_TYPE_SUB_HOUSE_END          = PIC_TYPE_SUB_HOUSE_RealMap;
    // picture size
    static public int   PIC_SIZE_ALL        = 0,
                        PIC_SIZE_Small      = 2,
                        PIC_SIZE_Moderate   = 3,
                        PIC_SIZE_Large      = 4;
}