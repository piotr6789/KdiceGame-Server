package Models;

class FieldModel
{
    private int _cubesCount;
    private int _ownerId;

    FieldModel()
    {
        _cubesCount = 0;
        _ownerId = 0;
    }


    int getCubesCount() {
        return _cubesCount;
    }

    void setCubesCount(int _cubesCount) {
        this._cubesCount = _cubesCount;
    }

    int getOwnerId() {
        return _ownerId;
    }

    void setOwnerId(int _ownerId) {
        this._ownerId = _ownerId;
    }
}
