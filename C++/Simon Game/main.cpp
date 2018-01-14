// Neeka Ebrahimi & Jared Shaw
// A5 - Simon Game
// 2/28/2016
// Description: This main function is the medium between the view and the model,
// establishes the connections neccessary for them to communicate

#include <QApplication>
#include <QWidget>
#include "view.h"
#include "model.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    View window;
    window.show();

    Model game;

    //signals view->model
    QObject::connect(&window, SIGNAL(StartGame()), &game, SLOT(StartGame()));
    QObject::connect(&window, SIGNAL(GetComputerTurn()), &game, SLOT(CalcCompTurn()));
    QObject::connect(&window, SIGNAL(SelectionMade(int)), &game, SLOT(ReceiveSelection(int)));

    //signals model->view
    QObject::connect(&game, SIGNAL(ValueChanged(std::vector<int>)), &window, SLOT(DisplayCompTurn(std::vector<int>)));
    QObject::connect(&game, SIGNAL(SelectionCorrect(bool)), &window, SLOT(CheckPlayerCorrect(bool)));

    return a.exec();
}
