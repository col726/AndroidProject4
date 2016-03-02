// RemoteUFOServiceReporter.aidl
package mckenna.colin.hw4;

// Declare any non-default types here with import statements

import mckenna.colin.hw4.UFOPosition;

interface RemoteUFOServiceReporter {

     void report(in List<UFOPosition> ships);
}
