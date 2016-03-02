// UFOPosition.aidl
package mckenna.colin.hw4;

// Declare any non-default types here with import statements

import mckenna.colin.hw4.RemoteUFOServiceReporter;

interface RemoteUFOService {

    void reset();
    void add(RemoteUFOServiceReporter reporter);
    void remove(RemoteUFOServiceReporter reporter);
}
