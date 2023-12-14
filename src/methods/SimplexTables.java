package methods;
import api.methods.ISimplexTables;
import utils.Table;

import java.util.ArrayList;
import java.util.List;
public class SimplexTables implements ISimplexTables {
    private String mode;
    @Override
    public void calculateNextTable(List<List<Float>> previousTable, List<Float> previousZ, List<Float> previousB) {
        List<List<Float>> newTable = previousTable;
        List<Float>  newZ = previousZ;
        List<Float> newB = previousB;
        // PART 1, Pivot line adjustment
        int pivotColIndex = getPivotCol(previousZ);
        int pivotRowIndex = getPivotRow(previousTable,previousZ,previousZ);
        float pivot = getPivot(previousTable,previousZ,previousB);
        List<Float> pivotLine = previousTable.get(pivotRowIndex);
        List<Float> newPivotLine = new ArrayList<>();
        for(float value : pivotLine){
            newPivotLine.add(value/pivot);
        }
        // new pivot line is ready
        newTable.set(pivotRowIndex,newPivotLine);
        newB.set(pivotRowIndex,previousB.get(pivotRowIndex)/pivot);
        // other lines
        for(int i = 0; i < previousTable.size();i++) {
            if(!(i == pivotRowIndex)){
                for(int j = 0; j < previousTable.get(0).size();j++){
                    if(!(j == pivotColIndex)){
                        float newElement =  previousTable.get(i).get(j) - pivotLine.get(j) * previousTable.get(i).get(pivotColIndex) / pivot;
                        newTable.get(i).set(j,newElement);
                    }
                    else{
                        newTable.get(i).set(j,0f);
                    }
                }
            }
        }
        // B
        for(int i = 0; i < previousB.size();i++){
            if(i == pivotRowIndex){
                newB.set(pivotRowIndex, previousB.get(pivotRowIndex)/pivot);
            }
            else{
                newB.set(i,previousB.get(i) - previousB.get(pivotRowIndex) * previousTable.get(i).get(pivotColIndex));
            }
        }
        // Z
        for(int i = 0; i < previousZ.size(); i++){
            if(!(i == pivotColIndex)){
                newZ.set(i,previousZ.get(i) - pivotLine.get(i) * previousTable.get(i).get(pivotColIndex) / pivot);
            }
            else{
                newZ.set(i,0f);
            }
        }
        // SET
        Table.table = newTable;
        Table.z = newZ;
        Table.b = newB;
    }
    @Override
    public float getPivot(List<List<Float>> current,List<Float> currentZ, List<Float> currentB) {
        return current.get(getPivotRow(current,currentZ,currentB)).get(getPivotCol(currentZ));
    }
    @Override
    public int getPivotRow(List<List<Float>> current,List<Float> currentZ, List<Float> currentB) {
        int pivotLineIndex = 0;
        int pivotColIndex = getPivotCol(currentZ);
        List<Float> values = new ArrayList<>();
        List<Float> result = new ArrayList<>();
        for(List<Float> line : current){
            values.add(line.get(pivotColIndex));
        }
        for(int i = 0; i < currentB.size() - 1;i++){
            result.add(values.get(i) == 0 ? Float.MAX_VALUE : currentB.get(i) / values.get(i));
        }
        float minValue = result.get(0);

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i) < minValue && result.get(i) >= 0) {
                minValue = result.get(i);
                pivotLineIndex = i;
            }
        }
        return pivotLineIndex;
    }
    @Override
    public int getPivotCol(List<Float> currentZ) {
        int pivotColIndex = 0;
        int tempIndex = 0;
        float tmp = currentZ.get(tempIndex);
        for(float coefficient : currentZ){
            if ((mode.equals("max") && coefficient > tmp) || (mode.equals("min") && coefficient < tmp)) {
                tmp = coefficient;
                pivotColIndex = tempIndex;
            }
            tempIndex++;
        }
        return pivotColIndex;
    }
}