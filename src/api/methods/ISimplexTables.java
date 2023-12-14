package api.methods;

import java.util.List;

public interface ISimplexTables {
    void calculateNextTable(List<List<Float>> previous,List<Float> previousZ, List<Float> previousB);
    float getPivot(List<List<Float>> current,List<Float> currentZ, List<Float> currentB);
    int getPivotRow(List<List<Float>> current,List<Float> currentZ, List<Float> currentB);
    int getPivotCol(List<Float> currentZ);
}
