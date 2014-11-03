package networking;

import java.util.Date;

/**
 * Created by jc4512 on 01/11/14.
 */
public class LocalOpenGame {
    public int variantID;
    private Date creationDate;

    public LocalOpenGame(int variantID) {
        this.variantID = variantID;
        creationDate = new Date();
    }
}
