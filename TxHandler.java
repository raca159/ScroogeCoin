import java.util.*;

public class TxHandler {

  /**
   * Creates a public ledger whose current UTXOPool (collection of unspent
   * transaction outputs) is {@code utxoPool}. This should make a copy of utxoPool
   * by using the UTXOPool(UTXOPool uPool) constructor.
   */
  public UTXOPool ledger;

  public TxHandler(UTXOPool utxoPool) {
    this.ledger = new UTXOPool(utxoPool);

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

    boolean isValid = true;

    // check if inputs are available for spending
    // checking (1)
    for (int i = 0; i < tx.numInputs(); i++) {
      UTXO bufferUtxo = new UTXO(tx.getInput(i).prevTxHash, tx.getInput(i).outputIndex);
      if (!ledger.contains(bufferUtxo)) {
        isValid = false; // means that input are not part of unspent pool
      }
    }

    // checking if signature of every input is equal to 'hash' of data from previous
    // checking (2)
    for (int i = 0; i < tx.numInputs(); i++) {
      UTXO bufferUtxo = new UTXO(tx.getInput(i).prevTxHash, tx.getInput(i).outputIndex);
      Transaction.Output bufferOutsUsed = ledger.getTxOutput(bufferUtxo);
      boolean trueSignature = Crypto.verifySignature(bufferOutsUsed.address, tx.getRawDataToSign(i),
          tx.getInput(i).signature);
      if (!trueSignature) {
        isValid = false;
      }

    }

    // checking for double spending, check if a new utxo is already in a list of
    // news
    // checking (3)
    UTXOPool bufferPool = new UTXOPool(); // new transactions made
    for (int i = 0; i < tx.numInputs(); i++) {
      UTXO bufferUtxos = new UTXO(tx.getInput(i).prevTxHash, tx.getInput(i).outputIndex);
      if (bufferPool.contains(bufferUtxos)) {
        isValid = false;
      } else {
        newUtxos.add(bufferUtxos, tx.getOutput(i));
      }
    }

    // checking for non negative
    // checking (4) - CORRECT
    for (Transaction.Output bufferOut : tx.getOutputs()) {
      if (bufferOut.value < 0) {
        isValid = false;
      }
    }

    // checking if sum is equals means getting the outputs from previous and
    // checking if is equals to the outputs from now
    // checking (5)
    double inputSum = 0;
    double outputSum = 0;
    for (int i = 0; i < tx.numInputs(); i++) {
      UTXO bufferUtxo = new UTXO(tx.getInput(i).prevTxHash, tx.getInput(i).outputIndex);
      inputSum = inputSum + ledger.getTxOutput(bufferUtxo).value;
    }
    for (Transaction.Output bufferOuts : tx.getOutputs()) { // current outputs
      outputSum = outputSum + bufferOuts.value;
    }
    if (outputSum < inputSum) { // means they are not equal
      isValid = false;
    }

    return isValid;

  }

  /**
   * Handles each epoch by receiving an unordered array of proposed transactions,
   * checking each transaction for correctness, returning a mutually valid array
   * of accepted transactions, and updating the current UTXO pool as appropriate.
   */

  public Transaction[] handleTxs(Transaction[] possibleTxs) {

    Set<Transaction> possibleTrans = new HashSet<>();
    for (Transaction bufferTrans : possibleTxs) {
      if (isValidTx(bufferTrans)) {
        possibleTrans.add(bufferTrans); // selecting only good ones

        // must update pool of unspent
        // removing spent ones
        for (Transaction.Input ins : bufferTrans.getInputs()) {
          for (int i = 0; i < bufferTrans.numOutputs(); i++) {
            UTXO bufferNewUtxo = new UTXO(ins.prevTxHash, ins.outputIndex);
            ledger.removeUTXO(bufferNewUtxo); // was adding to UTXOpoll instead of removing
          }
        }
        // adding new unspent ones
        for (int i = 0; i < bufferTrans.numOutputs(); i++) {
          UTXO bufferNewUtxo = new UTXO(bufferTrans.getHash(), i);
          ledger.addUTXO(bufferNewUtxo, bufferTrans.getOutput(i));
        }

      }
    }

    Transaction[] possiblesTx = new Transaction[possibleTrans.size()];
    possiblesTx = possibleTrans.toArray(possiblesTx);

    return possiblesTx;
    // return possibleTxs;
  }

}
