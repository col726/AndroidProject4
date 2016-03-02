package mckenna.colin.hw4;

import java.util.List;

/**
 * Created by cmckenna on 11/7/2015.
 */
public class Result {
    private int statusCode;
    private List<UFOPosition> resultPositions;

    public Result(int statusCode, List<UFOPosition> resultPositions){
        this.statusCode = statusCode;
        this.resultPositions = resultPositions;
    }

    public int getStatusCode(){ return statusCode;}
    public List<UFOPosition> getResultPositions(){ return resultPositions;}
}
