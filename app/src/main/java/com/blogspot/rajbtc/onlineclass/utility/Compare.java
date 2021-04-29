package com.blogspot.rajbtc.onlineclass.utility;

import com.blogspot.rajbtc.onlineclass.dataclass.ClassData;
import com.blogspot.rajbtc.onlineclass.dataclass.InformationData;

import java.util.Comparator;

public class Compare implements Comparator<InformationData> {


    @Override
    public int compare(InformationData o1, InformationData o2) {
        return o1.getRoll().compareTo(o2.getRoll());
    }
}
