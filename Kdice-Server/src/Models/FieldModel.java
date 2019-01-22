package Models;

public class FieldModel
{
    private int _cubesCount;
    private int _ownerId;

    public FieldModel()
    {
        _cubesCount = 0;
        _ownerId = 0;
    }


    public int getCubesCount() {
        return _cubesCount;
    }

    public void setCubesCount(int _cubesCount) {
        this._cubesCount = _cubesCount;
    }

    public int getOwnerId() {
        return _ownerId;
    }

    public void setOwnerId(int _ownerId) {
        this._ownerId = _ownerId;
    }
}
