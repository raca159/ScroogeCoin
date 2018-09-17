public class TxHandler {

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public UTXOPool ledger;
    
    public TxHandler(UTXOPool utxoPool) {
        // IMPLEMENT THIS
        ledger = new UTXOPool(utxoPool);
        
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        // IMPLEMENT THIS
        boolean isValid = false;
        
        byte[] prevHash = tx.prevTxHash;
        ArrayList<Output> outsTx = (tx.getOutputs()).clone();
        
        // hashmap of transactions outputs to see if there is double spending
        HashMap<Output, int> doubleSpending = new HashMap<Ouput, int>();
        for(Output buffer : outsTx){
            doubleSpending.put(buffer, 0);
        }
        
        // check if outputs are in UTXO pool
        UTXO atualUtxo;
        ArrayList<UTXO> allUtxos = ledger.getAllUTXO();
        ArrayList<Output> allOuts = new ArrayList<Output>();
        // getting all possible outputs to check
        for(UTXO bufferUtxo : allUtxos){
            for(Output bufferOut : ledger.getTxOutput(bufferUtxo)){
                allOuts.add(bufferOut);
            }
        }
        
        
        
        
        
        return isValid;
        
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        
        ArrayList<Transaction> possibeTrans = new ArrayList<Transaction>();
        for(Transaction bufferTrans : possibleTxs){
            if(this.isValidTx(bufferTrans)){
                possibleTrans.add(bufferTxs);
            }
        }
        Transaction[] possiblesTx = new Transaction[possibeTrans.size()];
        possiblesTx = possibeTrans.toArray(possiblesTx);
        
        //return possiblesTx;
        return possibleTxs
    }

}
