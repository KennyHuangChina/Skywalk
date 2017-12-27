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

    ///////////////////////////////////////////////////////////////////////////////////
    //
    public static int   GET_HOUSE_DIG_LST_TYPE_BEGIN        = 0,
                        GET_HOUSE_DIG_LST_TYPE_ALL          = 0,
                        GET_HOUSE_DIG_LST_TYPE_RECOMMEND    = 1,
                        GET_HOUSE_DIG_LST_TYPE_DEDUCTED     = 2,
                        GET_HOUSE_DIG_LST_TYPE_NEW          = 3,
                        GET_HOUSE_DIG_LST_TYPE_END          = GET_HOUSE_DIG_LST_TYPE_NEW;
}
