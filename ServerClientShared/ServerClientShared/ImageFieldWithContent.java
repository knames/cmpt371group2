package ServerClientShared;

import java.io.Serializable;

/**
 * Created by wsv759 on 12/03/15.
 */
public class ImageFieldWithContent extends FileFieldWithContent{
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public ImageFieldWithContent(ImageFieldWithoutContent fieldWithoutContent) {
        super(fieldWithoutContent);
    }

    @Override
    public boolean contentMatchesType(Serializable content)
    {
        if (!super.contentMatchesType(content))
            return false;

        //TODO add extra tests?
        return true;
    }
}
