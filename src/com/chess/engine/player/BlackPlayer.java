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

public class BlackPlayer extends Player {
    public BlackPlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) {
        super(board,blackStandardLegalMoves, whiteStandardLegalMoves); //In the parent class the second input is a list of a legal moves, so for black we check the black standard legal moves
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {

        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()){
            //Blacks King Side Castle
            if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()){
                final Tile rookTile = this.board.getTile(7);

                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if (Player.calculateAttacksOnTile(5, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()){
                        //TO DO A CASTLE MOVE
                        kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 6, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
                    }
                }
            }
            //Whites Queen Side Castle
            if (!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() && !this.board.getTile(3).isTileOccupied()){
                final Tile rookTile = this.board.getTile(0);

                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if (Player.calculateAttacksOnTile(1, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(2, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(3, opponentsLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()){
                        //TO DO A CASTLE MOVE
                        kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 2, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
                    }
                }
            }
        }


        return ImmutableList.copyOf(kingCastles);
    }
}
