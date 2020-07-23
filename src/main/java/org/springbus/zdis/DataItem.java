package org.springbus.zdis;

import io.lettuce.core.ScanCursor;
import lombok.Data;

import java.util.List;

@Data
public class DataItem <T>{
    private List<T> dataList;
    private ScanCursor cursor;
}
