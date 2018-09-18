import java.util.ArrayList;
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
   * @return true if: (1) all outputs claimed by {@code tx} are in the current
   *         UTXO pool, (2) the signatures on each input of {@code tx} are valid,
   *         (3) no UTXO is claimed multiple times by {@code tx}, (4) all of
   *         {@code tx}s output values are non-negative, and (5) the sum of
   *         {@code tx}s input values is greater than or equal to the sum of its
   *         output values; and false otherwise.
   */

  public boolean isValidTx(Transaction tx) {
    // IMPLEMENT THIS
    boolean isValid = true;

    int lengthIns = tx.numInputs();
    ArrayList<UTXO> newUtxos = new ArrayList<UTXO>();
    for (int i = 0; i < lengthIns; i++) {
      newUtxos.add(new UTXO(tx.getInput(i).prevTxHash, i));
    }

    // hashmap of transactions outputs to see if there is double spending
    HashMap<UTXO, Integer> doubleSpending = new HashMap<UTXO, Integer>();
    for (UTXO buffer : newUtxos) {
      doubleSpending.put(buffer, 0);
    }

    // check if outputs are in UTXO pool
    ArrayList<UTXO> allUtxos = ledger.getAllUTXO();
    // getting all utxos and comparing if new ones already exist
    for (UTXO bufferUtxo : allUtxos) {
      for (UTXO bufferNewOnes : newUtxos) {
        if (bufferNewOnes.compareTo(bufferUtxo) !=0) {
          doubleSpending.put(bufferNewOnes, doubleSpending.get(bufferNewOnes) + 1);
        }
      }
    }

    // // checking (1)
    // if (doubleSpending.containsValue(1)) {
    //   isValid = false;
    // }
    // // checking (2)
    // for (Transaction.Input newIns : tx.getInputs()) {
    //   if (newIns.signature.equals(newIns.prevTxHash)) { // check if each input signature equals to the signature of the
    //                                                     // block before it
    //   } else {
    //     isValid = false;
    //   }
    // }
    // // checking (3)
    // if (doubleSpending.containsValue(2)) {
    //   isValid = false;
    // }
    // checking (4) - CORRECT
    for (Transaction.Output bufferOut : tx.getOutputs()) {
      if (bufferOut.value < 0) {
        isValid = false;
      }
    }
    // checking (5)
    // double sumIn = 0;
    // double sumOut = 0;
    // for (int i = 0; i < tx.numInputs(); i++) {
    //   UTXO buffers = new UTXO(tx.getInput(i).prevTxHash, i);
    //   sumIn = sumIn + ledger.getTxOutput(buffers).value;
    // }
    // for (Transaction.Output bufferOut : tx.getOutputs()) {
    //   sumOut = sumOut + bufferOut.value;
    // }
    // if (sumIn < sumOut) {
    //   isValid = false;
    // }

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
