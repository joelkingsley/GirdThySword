package com.code.codemercenaries.girdthysword.Objects;

/**
 * Created by Joel Kingsley on 27-04-2018.
 */

public class Version {
    String _id;
    String _name;
    String _lang;

    public Version(String _id, String _name, String _lang) {
        this._id = _id;
        this._name = _name;
        this._lang = _lang;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_lang() {
        return _lang;
    }

    public void set_lang(String _lang) {
        this._lang = _lang;
    }

    @Override
    public String toString() {
        return _name + " (" +
                _lang + ')';
    }
}
