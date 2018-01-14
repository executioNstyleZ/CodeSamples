// Neeka Ebrahimi & Jared Shaw
// A5 - Simon Game
// 2/28/2016
// Description: This class performs most of the game logic, such as computing the computers turns, checking
// the player's input, and producing sound at certain points.
// Song sonOfFlynn.mp3 taken from Tron Legacy movie.

#include "model.h"
#include <QSoundEffect>

//Constructor, initializes the variables needed
Model::Model()
{
    playerSequencePosition = 0;
    selection = 0;

    startSound.setSource(QUrl("qrc:/sounds/start.wav"));
    startSound.setVolume(1);

    buttonSound.setSource(QUrl("qrc:/sounds/beep.wav"));
    buttonSound.setVolume(1);

    loseSound.setSource(QUrl("qrc:/sounds/error.wav"));
    loseSound.setVolume(1);

    player->setMedia(QUrl("qrc:/sounds/sonOfFlynn.mp3"));
}

Model::~Model()
{
    delete player;
}

//this function is called when the start button has been clicked
//starts or restarts the game
void Model::StartGame()
{
        startSound.play();
        player->play();

        playerSequencePosition = 0;
        sequence.clear();
}

//this function calculates the computer's turn by
//using a random number to generate a selection will represents
//the blue or red button being chosen.
void Model::CalcCompTurn()
{
    startSound.play();

    //everytime the computer has a turn, the player choice index gets reset
    playerSequencePosition = 0;

    //this is where the computer selects a value between 0 and 1
    for(int i = 0; i < 1; i++)
    {
        selection = std::rand() % 2;
        sequence.push_back(selection);
    }
    emit ValueChanged(sequence);
}

//This function is called when the player clicks the red or blue button
//checks if the selection is correct and sends a signal back
void Model::ReceiveSelection(int choice)
{
    if(choice == sequence.at(playerSequencePosition))
    {
        buttonSound.play();

        playerSequencePosition++;
        emit SelectionCorrect(true);
    }
    else if(choice != sequence.at(playerSequencePosition))
    {
        loseSound.play();

        emit SelectionCorrect(false);
    }
}





