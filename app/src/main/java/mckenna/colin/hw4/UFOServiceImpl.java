package mckenna.colin.hw4;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UFOServiceImpl extends Service {

    private List<RemoteUFOServiceReporter> reporters = new ArrayList<>();
    
    private List<UFOPosition> ufoPositionList = new ArrayList<>();

    private RequestTask requestTask;
    private Result result;

    private ServiceThread serviceThread;

    public UFOServiceImpl() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        requestTask = new RequestTask();
        serviceThread = new ServiceThread();
        serviceThread.start();
        return binder;
    }

    private class ServiceThread extends Thread {
        @Override public void run() {
            List<UFOPosition> currPos;
            int i = 1;
            //get ship positions
            do {
                result = requestTask.doGet(i);
                currPos = result.getResultPositions();
                if(result.getStatusCode() != HttpStatus.NOT_FOUND) {
                    //add ship to list if not seen before
                    for (UFOPosition u : currPos) {
                        UFOPosition tmp = ufoExists(u.getShipNum());
                        if (tmp != null) {
                            ufoPositionList.get(u.getShipNum()).setLat(u.getLat());
                            ufoPositionList.get(u.getShipNum()).setLon(u.getLon());
                        } else
                            ufoPositionList.add(u);
                    }
                    //send list of ships to reporters
                    for (RemoteUFOServiceReporter reporter : reporters) {
                        try {
                            reporter.report(ufoPositionList);
                        } catch (RemoteException e) {
                            Log.e("RemoteService", "report error", e);
                        }
                    }
                }
                i++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while(result.getStatusCode() != HttpStatus.NOT_FOUND);

        }
}

    private UFOPosition ufoExists(int shipNum) {

        for(UFOPosition ship : ufoPositionList)
            if (shipNum == ship.getShipNum())
                return ship;
        return null;
    }

    RemoteUFOService.Stub binder = new RemoteUFOService.Stub() {
        @Override
        public void reset() throws RemoteException {
            resetCounter();
        }

        @Override
        public void add(RemoteUFOServiceReporter reporter) throws RemoteException {
            reporters.add(reporter);
        }

        @Override
        public void remove(RemoteUFOServiceReporter reporter) throws RemoteException {
            reporters.remove(reporter);
        }
    };


    private void resetCounter() {
        int n = 0;
    }


}