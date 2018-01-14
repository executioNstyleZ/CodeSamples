// Neeka Ebrahimi & Jared Shaw
// A5 - Simon Game
// 2/28/2016
// Description: This is the view class, although it is more a view-controller class.
// Its main functions are to receive input from the GUI into slots, and send signals to the model
// it also updates the GUI as neccessary.

#ifndef VIEW_H
#define VIEW_H

#include <QMainWindow>
#include <vector>

namespace Ui {
class View;
}

class View : public QMainWindow
{
    Q_OBJECT

public:
    explicit View(QWidget *parent = 0);
    ~View();

signals:
    void SelectionMade(int selection);
    void GetComputerTurn();
    void StartGame();

private slots:
    void on_redButton_clicked();
    void on_blueButton_clicked();
    void on_startButton_clicked();
    void DisplayCompTurn(std::vector<int> sequence);
    void CheckPlayerCorrect(bool correct);
    void on_readyButton_clicked();
    void on_progressBar_valueChanged(int value);
    void on_blueButton_pressed();
    void on_redButton_pressed();

private:
    Ui::View *ui;
    double numberCorrect;
    double totalNumSequence;
    void StartUpGui();
    void StartGameGui();
    void SetUpPlayerTurn();
    void UpdateProgress();
    void GameLost();
    void Delay(float time);
    void RedButtonColor();
    void BlueButtonColor();
    float speed;
    int round;
};

#endif // VIEW_H
