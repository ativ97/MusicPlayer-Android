// player.aidl
package uic.edu.clipserver;

// Declare any non-default types here with import statements

interface player {
    void play(int index);
    void stop();
    int pause();
    void resume(int legnth);
}
