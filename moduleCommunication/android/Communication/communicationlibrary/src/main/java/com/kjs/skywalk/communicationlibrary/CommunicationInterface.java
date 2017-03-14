package com.kjs.skywalk.communicationlibrary;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/20.
 */

public class CommunicationInterface {
    public interface CICommandListener {
        void onCommandFinished(final String command, final IApiResults.ICommon res);
    }

    public interface CIProgressListener {
        void onProgressChanged(final String command, final String percent, HashMap<String, String> map);
    }

    public interface ICommand {
        int RecommendHouse(int house_id, int action);

        int GetPropertyListByName(String sName, int nBegin, int nCount);
        int AddProperty(String sName, String sAddr, String sDesc);
        int GetPropertyInfo(int nPropId);
        int ModifyPropertyInfo(int nPropId, String sName, String sAddr, String sDesc);

        int AddDeliverable(String sName);
        int GetDeliverableList();
        int AddHouseDeliverable(int house_id, int deliverable_id, int qty, String sDesc);
        int GetHouseDeliverables(int house_id);

        int AddFacilityType(String sTypeName);
        int GetFacilityTypeList();
        int AddFacility(int nType, String sName);
        int GetFacilityList(int nType);
    }
}
