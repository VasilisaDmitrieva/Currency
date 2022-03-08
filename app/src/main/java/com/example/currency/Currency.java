package com.example.currency;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Currency implements Parcelable {
    @SerializedName("CharCode")
    private final String charCode;
    @SerializedName("Nominal")
    private final Integer nominal;
    @SerializedName("Name")
    private final String name;
    @SerializedName("Value")
    private final Double value;

    public Currency(String charCode, Integer nominal, String name, Double value) {
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
    }

    public Currency(Parcel source) {
        value = source.readDouble();
        name = source.readString();
        nominal = source.readInt();
        charCode = source.readString();
    }

    @Override
    public String toString() {
        return charCode + '\n' + nominal + ' ' + name + " = " + value + " рублей";
    }

    public String getCharCode() {
        return charCode;
    }

    public Integer getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(charCode);
        parcel.writeInt(nominal);
        parcel.writeString(name);
        parcel.writeDouble(value);
    }

    public static final Parcelable.Creator<Currency> CREATOR = new Parcelable.Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel source) {
            return new Currency(source);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };
}
