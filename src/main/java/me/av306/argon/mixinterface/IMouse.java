package me.av306.argon.mixinterface;

// yeah naming conventions are messed up
public interface IMouse
{
    void accelerateDeltaX( double dx );
    void accelerateDeltaY( double dy );

    void changeX( double dx );
    void changeY( double dy );
}