package com.apps.jaredshaw.hotseatbattleships

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*

/**
 * Created by Jared on 11/21/2017.
 */
class GameActivity: AppCompatActivity(), AdapterView.OnItemClickListener, View.OnClickListener {


    lateinit private var gameViewAdapter: GridAdapter
    private lateinit var rootLayout: LinearLayout
    private lateinit var gameView: GridView
    private var spectateMode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras: Bundle = intent.extras
        spectateMode = extras.getInt("SPECTATE", -1)

        Log.i("SPECT", spectateMode.toString())

        rootLayout = LinearLayout(this)
        rootLayout.orientation = LinearLayout.VERTICAL
        setContentView(rootLayout)

        Database.listenToSingleGame(this, GameList.getCurrentGameInfo().getGameID().toString())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Database.removeListenerToSingleGame()
    }

    @SuppressLint("ResourceType")
    fun displayGameView(){

        if(GameList.isGameCompleted() == false && GameList.nameOfCurrentTurn() == Database.getEmail()){
            val toasty = Toast.makeText(this, "Its your turn!", Toast.LENGTH_SHORT)
            toasty.show()
        }

        rootLayout.removeAllViews()

        val stateText = TextView(this)
        stateText.setText("State: ${GameList.currentGameState()}")
        stateText.gravity = Gravity.CENTER
        stateText.textSize = 20.0f

        val playerEmails = GameList.getPlayerNames()
        val player1Name = TextView(this)
        player1Name.setText("Player 1: " + playerEmails.first + ", Ships Left: ${GameList.shipsLeft(1)}")
        player1Name.gravity = Gravity.LEFT

        val player2Name = TextView(this)
        player2Name.setText("Player 2: " + playerEmails.second + ", Ships Left: ${GameList.shipsLeft(2)}")
        player2Name.gravity = Gravity.LEFT

        gameView = android.widget.GridView(this)
        gameView.horizontalSpacing = 5
        gameView.verticalSpacing = 5
        gameView.setOnItemClickListener(this)
        gameView.numColumns = 10
        gameView.columnWidth = 100
        gameView.gravity = Gravity.CENTER
        gameView.stretchMode = GridView.STRETCH_SPACING_UNIFORM

        if(spectateMode == 1){
            gameViewAdapter = GridAdapter(this, true)
        }else{
            gameViewAdapter = GridAdapter(this, false)
        }
        gameView.adapter = gameViewAdapter

        val currentTurnText = TextView(this)
        if(GameList.isGameCompleted()){
            currentTurnText.setText(GameList.playerWonMessage())

            if(GameList.getNameOfLoser() == Database.getEmail()){
                val toasty = Toast.makeText(this, "You Lost! :(", Toast.LENGTH_SHORT)
                toasty.show()
            }

        }else{
            currentTurnText.setText("Current Turn: ${GameList.nameOfCurrentTurn()}")
        }
        currentTurnText.gravity = Gravity.CENTER

        val mainMenuButton = Button(this)
        mainMenuButton.setText("Main Menu")
        mainMenuButton.setOnClickListener(this)
        mainMenuButton.id = 1000

        rootLayout.addView(stateText, linearLayout("match", "match", 3.0f))
        rootLayout.addView(player1Name, linearLayout("match", "match", 3.0f))
        rootLayout.addView(player2Name, linearLayout("match", "match", 3.0f))
        rootLayout.addView(gameView, linearLayout("match", "match", 1.0f))
        rootLayout.addView(currentTurnText, linearLayout("match", "match", 3.0f))
        rootLayout.addView(mainMenuButton, linearLayout("match", "match",3.0f))
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        if(spectateMode != 0) {
            val toasty = Toast.makeText(this, "You're a spectator remember!", Toast.LENGTH_SHORT)
            toasty.show()
            return
        }

        if(GameList.isGameCompleted()){
            val toasty = Toast.makeText(this, "Game is already over, *sigh*", Toast.LENGTH_SHORT)
            toasty.show()
            return
        }

        if(GameList.currentGameState() == "JustStarting"){
            val toasty = Toast.makeText(this, "Waiting for an opponent", Toast.LENGTH_SHORT)
            toasty.show()
            return
        }

        if(GameList.nameOfCurrentTurn() != Database.getEmail()){
            val toasty = Toast.makeText(this, "Its not your turn!", Toast.LENGTH_SHORT)
            toasty.show()
            return
        }

        if(position > 99) {
            val toasty = Toast.makeText(this, "You cannot fire on your own territory", Toast.LENGTH_SHORT)
            toasty.show()
            return
        }




        displayTurnSummary(position)
    }

    override fun onClick(clickedView: View?) {

        if(clickedView !is View) return

        //main menu button
        if(clickedView.id == 1000){
            val myIntent = Intent(this, ListActivity::class.java)
            Database.removeListenerToSingleGame()
            Database.updateCurrentGame()
            startActivity(myIntent)
        }

        //end turn button
        else if(clickedView.id == 200){
            Database.updateCurrentGame()
        }
    }

    fun linearLayout(firstParam: String, secondParam: String, gravity: Float): LinearLayout.LayoutParams{

        var first: Int
        var second: Int

        if(firstParam == "match"){
            first = LinearLayout.LayoutParams.MATCH_PARENT
        }
        else{
            first = LinearLayout.LayoutParams.WRAP_CONTENT
        }

        if(secondParam == "match"){
            second = LinearLayout.LayoutParams.MATCH_PARENT
        }else{
            second = LinearLayout.LayoutParams.WRAP_CONTENT
        }

        return LinearLayout.LayoutParams(first, second, gravity)
    }

    @SuppressLint("ResourceType")
    private fun displayTurnSummary(attackPosition: Int){
        rootLayout.removeAllViews()

        var builder = StringBuilder()

        val playerName = TextView(this)
        builder.append("${GameList.nameOfCurrentTurn()}\n")
        playerName.setText(builder.toString())
        playerName.gravity = Gravity.CENTER
        playerName.textSize = 20.0f

        val summaryTextView = TextView(this)
        builder.setLength(0)
        builder.append("Your attack on position ${attackPosition}\n")
        builder.append("resulted in a ${GameList.launchMissle(GameList.getCurrentTurn(), attackPosition)}")
        summaryTextView.setText(builder.toString())
        summaryTextView.gravity = Gravity.CENTER

        val sunkenShip = TextView(this)
        sunkenShip.gravity = Gravity.CENTER
        builder.setLength(0)

        val enemyShipsLeft = GameList.shipsLeft(GameList.getOppositePlayer(GameList.getCurrentTurn()))

        val howManyToGO = TextView(this)
        howManyToGO.gravity = Gravity.CENTER
        builder.setLength(0)
        builder.append("The enemy fleet still spans ${enemyShipsLeft} grid location/s")
        howManyToGO.setText(builder.toString())

        rootLayout.addView(playerName, linearLayout("match","match",3.0f))
        rootLayout.addView(summaryTextView, linearLayout("match","match",3.0f))
        rootLayout.addView(howManyToGO, linearLayout("match","match",3.0f))

        if(enemyShipsLeft > 0){
            GameList.switchTurn()

            val nextPlayerTurnText = TextView(this)
            builder.setLength(0)
            builder.append("Next turn is: ${GameList.nameOfCurrentTurn()}")
            nextPlayerTurnText.setText(builder.toString())
            nextPlayerTurnText.gravity = Gravity.CENTER

            rootLayout.addView(nextPlayerTurnText, linearLayout("match","match",3.0f))

            val endTurnButton = Button(this)
            endTurnButton.setText("End Turn")
            endTurnButton.setOnClickListener(this)
            endTurnButton.id = 200

            rootLayout.addView(endTurnButton, linearLayout("match", "match", 1.0f))

            val mainMenuButton = Button(this)
            mainMenuButton.setText("Main Menu")
            mainMenuButton.setOnClickListener(this)
            mainMenuButton.id = 1000
            rootLayout.addView(mainMenuButton, linearLayout("match", "wrap", 1.0f))
        }else{
            val congratsMessage = TextView(this)
            congratsMessage.gravity = Gravity.CENTER
            builder.setLength(0)
            builder.append("Congratulations, you have defeated Player ${GameList.getOppositePlayer(GameList.getCurrentTurn())}.\n")
            builder.append("Your fleet was still in ${GameList.shipsLeft(GameList.getCurrentTurn())} grid locations.")
            congratsMessage.setText(builder.toString())

            rootLayout.addView(congratsMessage, linearLayout("match","match",3.0f))

            val mainMenuButton = Button(this)
            mainMenuButton.setText("Main Menu")
            mainMenuButton.setOnClickListener(this)
            mainMenuButton.id = 1000
            rootLayout.addView(mainMenuButton, linearLayout("match", "match", 1.0f))
        }


    }

    override fun onDestroy() {
        super.onDestroy()

        Database.removeListenerToSingleGame()
        Database.updateCurrentGame()
    }

    override fun onStop() {
        super.onStop()

        Database.updateCurrentGame()
    }

}