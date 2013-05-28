package controller.action.ui;

import common.Log;
import common.Tools;
import controller.EventHandler;
import controller.action.ui.penalty.Penalty;
import controller.action.ui.penalty.PickUp;
import controller.action.GCAction;
import controller.action.ActionType;
import data.AdvancedData;
import data.HL;
import data.GameControlData;
import data.PlayerInfo;
import data.Rules;

/**
 * @author: Michel Bartsch
 * 
 * This action means that a player has been selected.
 */
public class Robot extends GCAction
{
    /** On which side (0:left, 1:right) */
    private int side;
    /** The players`s number, beginning with 0! */
    private int number;
    
    /**
     * Creates a new Robot action.
     * Look at the ActionBoard before using this.
     * 
     * @param side      On which side (0:left, 1:right)
     * @param number    The players`s number, beginning with 0!
     */
    public Robot(int side, int number)
    {
        super(ActionType.UI);
        this.side = side;
        this.number = number;
    }

    /**
     * Performs this action to manipulate the data (model).
     * 
     * @param data      The current data to work on.
     */
    @Override
    public void perform(AdvancedData data)
    {
        PlayerInfo player = data.team[side].player[number];
        if(EventHandler.getInstance().lastUIEvent instanceof Penalty) {
            EventHandler.getInstance().lastUIEvent.performOn(data, player, side, number);
        } else if(player.penalty != PlayerInfo.PENALTY_NONE) {
            player.penalty = PlayerInfo.PENALTY_NONE;
            Log.state(data, "Unpenalised "+
                Rules.league.teamColorName[data.team[side].teamColor]
                + " " + (number+1));
        }
    }
    
    /**
     * Checks if this action is legal with the given data (model).
     * Illegal actions are not performed by the EventHandler.
     * 
     * @param data      The current data to check with.
     */
    @Override
    public boolean isLegal(AdvancedData data)
    {
        return data.team[side].player[number].penalty != PlayerInfo.PENALTY_NONE && data.getRemainingPenaltyTime(side, number) == 0
                || EventHandler.getInstance().lastUIEvent instanceof PickUp
                || data.team[side].player[number].penalty == PlayerInfo.PENALTY_NONE
                    && EventHandler.getInstance().lastUIEvent instanceof Penalty
                || data.testmode;
    }
}