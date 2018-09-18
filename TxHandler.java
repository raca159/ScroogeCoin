import java.util.ArrayList;

import Transaction.Input;

import java.util.*;

public class TxHandler {

  /**
   * Creates a public ledger whose current UTXOPool (collection of unspent
   * transaction outputs) is {@code utxoPool}. This should make a copy of utxoPool
   * by using the UTXOPool(UTXOPool uPool) constructor.
   */
  public UTXOPool ledger;

  public TxHandler(UTXOPool utxoPool) {
    ledger = new UTXOPool(utxoPool);

  }

  /**
   * @return true if:
   *  (1) all outputs claimed by {@code tx} are in the current UTXO pool,
   *  (2) the signatures on each input of {@code tx} are valid,
   *  (3) no UTXO is claimed multiple times by {@code tx},
   *  (4) all of {@code tx}s output values are non-negative, and
   *  (5) the sum of {@code tx}s input values is greater than or equal to the sum of its
   *         output values; and false otherwise.
   */

  public boolean isValidTx(Transaction tx) {

    boolean isValid = true;

    // new transactions made
    ArrayList<UTXO> newUtxos = new ArrayList<UTXO>();
    for (int i = 0; i < tx.numInputs(); i++) {
      newUtxos.add(new UTXO(tx.getInput(i).prevTxHash, tx.getInput(i).outputIndex));
    }

    // check if inputs are available for spending
    // checking (1)
    for (UTXO bufferUtxo : newUtxos) {
      if(!ledger.contains(bufferUtxo)){
        isValid = false; // means that input are not part of unspent pool
      }
    }

    // checking if signature of every input is equal to 'hash' of data from previous
    // checking (2)
    for (int i = 0; i < tx.numInputs(); i++) {
      UTXO bufferUtxo = new UTXO(tx.getInput(i).prevTxHash, tx.getInput(i).outputIndex);
      Transaction.Output bufferOutsUsed = ledger.getTxOutput(bufferUtxo);
      boolean trueSignature = Crypto.verifySignature(bufferOutsUsed.address, tx.getRawDataToSign(i), tx.getInput(i).signature);
      if (!trueSignature){
        isValid = false;
      }
    
    }

    // checking for non negative
    // checking (4) - CORRECT
    for (Transaction.Output bufferOut : tx.getOutputs()) {
      if (bufferOut.value < 0) {
        isValid = false;
      }
    }


    return isValid;

  }

  /**
   * Handles each epoch by receiving an unordered array of proposed transactions,
   * checking each transaction for correctness, returning a mutually valid array
   * of accepted transactions, and updating the current UTXO pool as appropriate.
   */

  public Transaction[] handleTxs(Transaction[] possibleTxs) {

    ArrayList<Transaction> possibleTrans = new ArrayList<Transaction>();
    for (Transaction bufferTrans : possibleTxs) {
      if (this.isValidTx(bufferTrans)) {
        possibleTrans.add(bufferTrans);
      }
    }
    Transaction[] possiblesTx = new Transaction[possibleTrans.size()];
    possiblesTx = possibleTrans.toArray(possiblesTx);

    return possiblesTx;
    // return possibleTxs;
  }

}
