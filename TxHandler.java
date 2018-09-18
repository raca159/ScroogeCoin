import java.util.ArrayList;

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
    boolean isValid = false;

    byte[] prevHash = tx.prevTxHash;
    ArrayList<Input> insTx = (tx.getInputs()).clone();
    int lengthIns = tx.numInputs;
    ArrayList<UTXO> newUtxos = new ArrayList<UTXO>();
    for (int i = 0; i < lengthIns; i++) {
      newUtxos.add(new UTXO(prevHash, i));
    }

    // hashmap of transactions outputs to see if there is double spending
    HashMap<UTXO, Int> doubleSpending = new HashMap<UTXO, Int>();
    for (UTXO buffer : newUtxos) {
      doubleSpending.put(buffer, 0);
    }

    // check if outputs are in UTXO pool
    ArrayList<UTXO> allUtxos = ledger.getAllUTXO();
    ArrayList<Output> allOuts = new ArrayList<Output>();
    // getting all utxos and comparing if new ones already exist
    for (UTXO bufferUtxo : allUtxos) {
      for (UTXO bufferNewOnes : newUtxos) {
        if (bufferNewOnes.equals(bufferUtxo)) {
          doubleSpending.put(bufferNewOnes, doubleSpending.get(bufferNewOnes) + 1);
        }
      }
    }

    // checking (1)
    if (doubleSpending.containsValue(1)) {
      isValid = false;
    }
    // checking (2)
    for (Input newIns : insTx) {
      if (newIns.signature.equals) { // check if each input signature equals to the signature of the block before it
      }
    }
    // checking (3)
    if (doubleSpending.containsValue(2)) {
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

    ArrayList<Transaction> possibeTrans = new ArrayList<Transaction>();
    for (Transaction bufferTrans : possibleTxs) {
      if (this.isValidTx(bufferTrans)) {
        possibleTrans.add(bufferTxs);
      }
    }
    Transaction[] possiblesTx = new Transaction[possibeTrans.size()];
    possiblesTx = possibeTrans.toArray(possiblesTx);

    // return possiblesTx;
    return possibleTxs;
  }

}
