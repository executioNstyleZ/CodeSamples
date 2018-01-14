// Neeka Ebrahimi & Jared Shaw
// A5 - Simon Game
// 2/28/2016
// Description: This is the view class, although it is more a view-controller class.
// Its main functions are to receive input from the GUI into slots, and send signals to the model
// it also updates the GUI as neccessary.

#include "view.h"
#include "ui_view.h"
#include "model.h"
#include <vector>
#include <QThread>
#include <QTime>

View::View(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::View)
{
    ui->setupUi(this);

    numberCorrect = 0;
    totalNumSequence = 0;
    StartUpGui();
}

View::~View()
{
    delete ui;
}

/*********************************** GUI STATES ************************************************************/

//sets up the GUI by making all the buttons invisible
// at the time of startup
void View::StartUpGui()
{
    ui->progressBar->setValue(0);
    ui->progressBar->setVisible(false);

    ui->turnLabel->setVisible(false);

    ui->redButton->setVisible(false);

    ui->blueButton->setVisible(false);

    ui->readyButton->setVisible(false);

    ui->roundNumber->setVisible(false);

    ui->startButton->setStyleSheet("background-color: rgb(255, 255, 255); border-style: outset; border-width: 4px; color: black; font: 16pt MS Sans Serif;");
    ui->startButton->setVisible(true);
}

//Sets up the GUI for the computer by making everything visible
//and disabling the red and blue buttons
void View::StartGameGui()
{
    ui->blueButton->setStyleSheet("border-color: rgb(0, 0, 255); background-color: rgb(0, 0, 0); border-style: outset; border-width: 4px; color: blue; font: 24pt 'Showcard Gothic';");
    ui->blueButton->setVisible(true);
    ui->blueButton->setEnabled(false);

    ui->redButton->setStyleSheet("border-color: rgb(255, 0, 0); background-color: rgb(0, 0, 0); border-style: outset; border-width: 4px; color: red; font: 24pt 'Showcard Gothic';");
    ui->redButton->setVisible(true);
    ui->redButton->setEnabled(false);

    ui->turnLabel->setStyleSheet("background-color: rgb(0, 0, 0); border-style: outset; border-width: 4px; color: white; font: 20pt MS Sans Serif;");
    ui->turnLabel->setText("Computer's Turn");
    ui->turnLabel->setVisible(true);

    ui->readyButton->setStyleSheet("background-color: rgb(255, 255, 255); border-style: outset; border-width: 4px; color: black; font: 16pt MS Sans Serif;");
    ui->readyButton->setText("I'm Ready!");
    ui->readyButton->setVisible(true);
    ui->readyButton->setEnabled(true);

    ui->progressBar->setValue(0);
    ui->progressBar->setStyleSheet("color:green;");
    ui->progressBar->setVisible(true);

    ui->roundNumber->display(round);
    ui->roundNumber->setStyleSheet("color:green;");
    ui->roundNumber->setVisible(true);

    ui->startButton->setText("Restart");
}

//Sets the GUI up ready for the player to click
//the red and blue buttons
void View::SetUpPlayerTurn()
{
    ui->blueButton->setEnabled(true);

    ui->redButton->setEnabled(true);

    ui->readyButton->setStyleSheet("background-color: rgb(0, 0, 0); border-style: outset; border-width: 4px; color: grey;");
    ui->readyButton->setEnabled(false);

    ui->turnLabel->setText("Player's Turn");

    ui->startButton->setEnabled(true);
    ui->startButton->setStyleSheet("background-color: rgb(255, 255, 255); border-style: outset; border-width: 4px; color: black; font: 16pt MS Sans Serif;");

    numberCorrect = 0;

    round++;
}

//updates the display on the progress bar
void View::UpdateProgress()
{
    double percentValue = (numberCorrect/totalNumSequence) * 100;
    ui->progressBar->setValue(percentValue);
}

//Is called when the player makes a wrong selection
//disables the buttons and displays a lose message
void View::GameLost()
{
    ui->blueButton->setEnabled(false);
    ui->redButton->setEnabled(false);
    ui->startButton->setText("Start");
    ui->turnLabel->setText("That was not the correct sequence, you lose!");
}

void View::RedButtonColor()
{
    ui->redButton->setStyleSheet("border-color: rgb(255, 255, 255); background-color: rgb(255, 0, 0); border-style: outset; border-width: 4px; color: white; font: 24pt 'Showcard Gothic';");
    ui->redButton->style()->unpolish(ui->redButton);
    ui->redButton->style()->polish(ui->redButton);
    ui->redButton->update();

    Delay(speed);

    ui->redButton->setStyleSheet("border-color: rgb(255, 0, 0); background-color: rgb(0, 0, 0); border-style: outset; border-width: 4px; color: red; font: 24pt 'Showcard Gothic';" );
    ui->redButton->style()->unpolish(ui->redButton);
    ui->redButton->style()->polish(ui->redButton);
    ui->redButton->update();
}

void View::BlueButtonColor()
{
    ui->blueButton->setStyleSheet("border-color: rgb(255, 255, 255); background-color: rgb(0, 0, 255); border-style: outset; border-width: 4px; color: white; font: 24pt 'Showcard Gothic';");
    ui->blueButton->style()->unpolish(ui->blueButton);
    ui->blueButton->style()->polish(ui->blueButton);
    ui->blueButton->update();

    Delay(speed);

    ui->blueButton->setStyleSheet("border-color: rgb(0, 0, 255); background-color: rgb(0, 0, 0); border-style: outset; border-width: 4px; color: blue; font: 24pt 'Showcard Gothic';");
    ui->blueButton->style()->unpolish(ui->blueButton);
    ui->blueButton->style()->polish(ui->blueButton);
    ui->blueButton->update();
}



/************************ Functions **************************/



//displays the computer's sequence of selections
//is called when the model sends the computer turn signal
void View::DisplayCompTurn(std::vector<int> sequence)
{
    ui->startButton->setEnabled(false);
    ui->startButton->setStyleSheet("background-color: rgb(0, 0, 0); border-style: outset; border-width: 4px; color: grey; font: 16pt MS Sans Serif;");

    ui->progressBar->setValue(0);
    ui->roundNumber->display(round);
    numberCorrect = 0;

    totalNumSequence = (int)sequence.size();

    speed = 1000/round;

    for(int i = 0; i < sequence.size(); i++)
    {
        Delay(speed);

        if(sequence.at(i) == 0)
        {
            RedButtonColor();
        }
        else if(sequence.at(i) == 1)
        {
            BlueButtonColor();
        }
    }


    SetUpPlayerTurn();
}

//Is called when after a player selection has been checked by the model
void View::CheckPlayerCorrect(bool correct)
{
    if(correct == true)
    {
        numberCorrect++;
        UpdateProgress();
    }
    else if(correct == false)
    {
        GameLost();
    }
}

//keeps track of the progress bar to know when a round is complete
void View::on_progressBar_valueChanged(int value)
{
    if(value == 100)
    {
        ui->redButton->setEnabled(false);
        ui->blueButton->setEnabled(false);

        ui->readyButton->setStyleSheet("background-color: rgb(255, 255, 255); border-style: outset; border-width: 4px; color: black;");
        ui->readyButton->setEnabled(true);

        ui->readyButton->setText("Next Round");
    }
}


/********************************* Button Clicks *****************************************/


//Sends a signal when the red button is clicked containing the players selection
void View::on_redButton_clicked()
{
    emit SelectionMade(0);
}

//called when red button was pressed, kept separate from clicked() for performance issues
void View::on_redButton_pressed()
{
    RedButtonColor();
}

//Sends a signal when the red button is clicked containing the players selection
void View::on_blueButton_clicked()
{
    emit SelectionMade(1);
}

//called when blue button is pressed, kept separeate from clicked() for performance issues
void View::on_blueButton_pressed()
{
    BlueButtonColor();
}

//called when the start button is clicked
void View::on_startButton_clicked()
{
    round = 1;
    StartGameGui();

    emit StartGame();
}

//the player must press the ready button, in order to display the computer's turn
void View::on_readyButton_clicked()
{
    ui->readyButton->setStyleSheet("background-color: rgb(0, 0, 0); border-style: outset; border-width: 4px; color: grey;");
    ui->readyButton->setEnabled(false);

    ui->turnLabel->setText("Computer's Turn");

    emit GetComputerTurn();
}


/****************************************** Helper functions **********************************/


//helper function that sets a delay
//reference: http://stackoverflow.com/questions/3752742/how-do-i-create-a-pause-wait-function-using-qt
void View::Delay(float time)
{
    QTime dieTime= QTime::currentTime().addMSecs(time);
        while (QTime::currentTime() < dieTime)
            QCoreApplication::processEvents(QEventLoop::AllEvents, 100);
}




