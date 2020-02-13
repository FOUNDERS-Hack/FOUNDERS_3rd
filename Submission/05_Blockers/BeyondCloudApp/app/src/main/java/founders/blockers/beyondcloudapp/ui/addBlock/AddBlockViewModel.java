package founders.blockers.beyondcloudapp.ui.addBlock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddBlockViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddBlockViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is addBlock fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}