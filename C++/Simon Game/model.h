// Neeka Ebrahimi & Jared Shaw
// A5 - Simon Game
// 2/28/2016
// Description: This class performs most of the game logic, such as computing the computers turns, checking
// the player's input, and producing sound at certain points.

#ifndef MODEL_H
#define MODEL_H

#include <vector>
#include <QObject>
#include <random>
#include <QSoundEffect>
#include <QMediaPlayer>

class Model : public QObject
{
    Q_OBJECT

public:
    Model();
    ~Model();

public slots:
    void StartGame();
    void CalcCompTurn();
    void ReceiveSelection(int choice);

signals:
    void ValueChanged(std::vector<int> sequence);
    void SelectionCorrect(bool trueOrFalse);

private:
    std::vector<int> sequence;
    int playerSequencePosition;
    int selection;
    QSoundEffect startSound;
    QSoundEffect loseSound;
    QSoundEffect buttonSound;
    QMediaPlayer *player = new QMediaPlayer;
};

#endif // MODEL_H
