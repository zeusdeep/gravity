package fi.zalando.core.persistence.mocks;

import fi.zalando.core.data.model.Dateable;
import io.realm.RealmObject;

/**
 * Mock object to test {@link fi.zalando.core.persistence.BaseRealmDAO}
 *
 * Created by jduran on 07/03/16.
 */
@SuppressWarnings("unused")
public class ValidWithoutPrimaryKeyRealmModel extends RealmObject implements Dateable {

    private long savedDate;

    public ValidWithoutPrimaryKeyRealmModel() {

    }

    @Override
    public long getSavedDate() {

        return savedDate;
    }

    @Override
    public void setSavedDate(long savedDate) {

        this.savedDate = savedDate;
    }

}
