package founders.blockers.beyondcloudapp.ui.findBlock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FindBlockViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FindBlockViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is findBlock fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}