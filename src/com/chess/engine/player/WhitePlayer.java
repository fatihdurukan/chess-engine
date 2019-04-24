package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player{


    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);

    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals) {

        /*
        -The king and the chosen rook are on the player's first rank.
        -Neither the king nor the chosen rook has previously moved.
        -There are no pieces between the king and the chosen rook.
        -The king is not currently in check.
        -The king does not pass through a square that is attacked by an enemy piece.
        -The king does not end up in check. (True of any legal move.)
         */

        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            //white king side castle
            if(!this.board.getTile(61).isTileOccupied() && this.board.getTile(62).isTileOccupied()){
                final Tile rookTile = this.board.getTile(63);

                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
                        rookTile.getPiece().getPieceType().isRook()) {
                        //TODO Add a castlemove!
                        kingCastles.add(null);
                    }
                }
            }
        }

        if(!this.board.getTile(59).isTileOccupied() &&
           !this.board.getTile(58).isTileOccupied()&&
           !this.board.getTile(57).isTileOccupied()){

            final Tile rookTile = this.board.getTile(56);

            if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                //TODO Add a castlemove!
                kingCastles.add(null);
            }

        }


        return ImmutableList.copyOf(kingCastles);
    }
}
