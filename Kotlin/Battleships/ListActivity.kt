package com.apps.jaredshaw.hotseatbattleships

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import android.widget.GridView

class ListActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemClickListener {

    private var listItemPosition: Int = -1
    lateinit private var gameListView: ListView
    lateinit private var rootLayout: LinearLayout
    private var inGameList = true

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rootLayout = LinearLayout(this)
        rootLayout.setBackgroundColor(Color.WHITE)
        rootLayout.orientation = LinearLayout.VERTICAL
        setContentView(rootLayout)

        Database.listenToGameList(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        Database.removeListenerToGameList()
    }

    @SuppressLint("ResourceType")
    fun displayMainMenu(){

        rootLayout.removeAllViews()
        inGameList = true

        gameListView = ListView(this)
        gameListView.adapter = GameListAdapter(this)
        gameListView.setOnItemClickListener(this)
        rootLayout.addView(gameListView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f))

        val buttonLayout = LinearLayout(this)
        buttonLayout.setBackgroundColor(Color.BLACK)
        buttonLayout.orientation = LinearLayout.HORIZONTAL

        val buttonLayout2 = LinearLayout(this)
        buttonLayout2.setBackgroundColor(Color.BLACK)
        buttonLayout2.orientation = LinearLayout.HORIZONTAL

        val newGameButton = Button(this)
        newGameButton.setText("Create")
        newGameButton.id = 100
        newGameButton.setOnClickListener(this)

        val continueGameButton = Button(this)
        continueGameButton.setText("Continue")
        continueGameButton.id = 101
        continueGameButton.setOnClickListener(this)

        val joinGameButton = Button(this)
        joinGameButton.setText("Join")
        joinGameButton.id = 102
        joinGameButton.setOnClickListener(this)

        val spectateGameButton = Button(this)
        spectateGameButton.setText("Spectate")
        spectateGameButton.id = 103
        spectateGameButton.setOnClickListener(this)

        val deleteGameButton = Button(this)
        deleteGameButton.setText("Delete")
        deleteGameButton.id = 104
        deleteGameButton.setOnClickListener(this)

        buttonLayout.addView(newGameButton, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f))
        buttonLayout.addView(deleteGameButton, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f))

        buttonLayout2.addView(continueGameButton, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f))
        buttonLayout2.addView(joinGameButton, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f))
        buttonLayout2.addView(spectateGameButton, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f))

        rootLayout.addView(buttonLayout, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 5.0f))
        rootLayout.addView(buttonLayout2, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 5.0f))
    }

    fun linearLayoutWrapContent(gravity: Float): LinearLayout.LayoutParams{
        return LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, gravity)
    }

    fun linearLayoutMatchParent(gravity: Float): LinearLayout.LayoutParams{
        return LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, gravity)
    }

    override fun onItemClick(p0: AdapterView<*>?, clickedItem: View?, position: Int, p3: Long) {
        if(clickedItem !is View) return

        if(inGameList) {
            listItemPosition = position
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onClick(clickedView: View?) {
        if(clickedView !is View) return

        //start new game
        if(clickedView.id == 100){

            GameList.startNewGame()

            var myIntent: Intent = Intent(this, GameActivity::class.java)
            myIntent.putExtra("SPECTATE", 0)

            Database.removeListenerToGameList()
            startActivity(myIntent)
        }

        //continue game
        else if(clickedView.id == 101){

            if(GameList.containsGame(listItemPosition)){

                val players = GameList.getPlayerNames(listItemPosition)

                if(players.first != Database.getEmail() && players.second != Database.getEmail()){
                    val toasty = Toast.makeText(this, "Unable to continue game: You are not one of the players", Toast.LENGTH_SHORT)
                    toasty.show()
                    return
                }

                GameList.continueGame(listItemPosition)
                var myIntent: Intent = Intent(this, GameActivity::class.java)
                myIntent.putExtra("SPECTATE", 0)
                Database.removeListenerToGameList()
                startActivity(myIntent)
            }
        }

        //join game
        else if(clickedView.id == 102){
            if(GameList.containsGame(listItemPosition)){
                val state = GameList.getGameState(listItemPosition)

                if(state != Game.GameState.JustStarting.toString() ){
                    val toasty = Toast.makeText(this, "Unable to join game: Game must be in 'JustStarting' state", Toast.LENGTH_SHORT)
                    toasty.show()
                    return
                }

                if(GameList.containsGame(listItemPosition)){
                    val playerNames = GameList.getPlayerNames(listItemPosition)

                    if(playerNames.second != "Waiting for opponent"){
                        val toasty = Toast.makeText(this, "Unable to join game: Already two players in this game", Toast.LENGTH_SHORT)
                        toasty.show()
                        return
                    }
                }else{
                    return
                }

                GameList.joinGame(listItemPosition)
                var myIntent: Intent = Intent(this, GameActivity::class.java)
                myIntent.putExtra("SPECTATE", 0)
                Database.removeListenerToGameList()
                startActivity(myIntent)
            }
        }

        //spectate game
        else if(clickedView.id == 103){
            if(GameList.containsGame(listItemPosition)) {


                if(GameList.getPlayerNames(listItemPosition).first == Database.getEmail() || GameList.getPlayerNames(listItemPosition).second == Database.getEmail()){
                    val toasty = Toast.makeText(this, "You can't spectate your own game!", Toast.LENGTH_SHORT)
                    toasty.show()
                    return
                }


                val gameState = GameList.getGameState(listItemPosition)
                if(gameState != Game.GameState.InProgress.toString()){
                    val toasty = Toast.makeText(this, "Unable to spectate game: Game must in 'InProgress' state", Toast.LENGTH_SHORT)
                    toasty.show()
                    return
                }



                GameList.spectateGame(listItemPosition)

                var myIntent: Intent = Intent(this, GameActivity::class.java)
                myIntent.putExtra("SPECTATE", 1)
                Database.removeListenerToGameList()
                startActivity(myIntent)
            }
        }

        //delete game
        else if(clickedView.id == 104){ //
            if(listItemPosition != -1) {

                if(GameList.containsGame(listItemPosition) == false) return
//TODO: remove comments
                /*
                val players = GameList.getPlayerNames(listItemPosition)
                if(players.first != Database.getEmail() && players.second != Database.getEmail()){
                    val toasty = Toast.makeText(this, "Unable to delete game: You can only delete games you were apart of", Toast.LENGTH_SHORT)
                    toasty.show()
                    return
                }

                val gameState = GameList.getGameState(listItemPosition)
                if(gameState != Game.GameState.Player2Won.toString() && gameState != Game.GameState.Player1Won.toString()){
                    val toasty = Toast.makeText(this, "Unable to delete game: Game must be completed first", Toast.LENGTH_SHORT)
                    toasty.show()
                    return
                }*/

                val gameID = GameList.deleteGameAt(listItemPosition)
                Database.deleteGame(this, gameID)
            }
        }

        //main menu
        else if(clickedView.id == 1000){
            displayMainMenu()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Database.removeListenerToGameList()
    }




}
