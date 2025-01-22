package com.example.grt3api.models;

public class Donation {
    public String name;
    public long amount;
    public String formatedAmount;
    public String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getFormatedAmount() {
        return formatedAmount;
    }

    public void setFormatedAmount(String formatedAmount) {
        this.formatedAmount = formatedAmount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}


/*
{
  "type": "donation",
  "message": [
    {
      "id": 96164121,
      "name": "test",
      "amount": "13.37",
      "formatted_amount": "$13.37",
      "formattedAmount": "$13.37",
      "message": "test donation",
      "currency": "USD",
      "emotes": null,
      "iconClassName": "user",
      "to": {
        "name": "Sai Harsha Maddela"
      },
      "from": "test",
      "from_user_id": null,
      "_id": "0820c9d5bafd768c9843f5e35c885e71"
    }
  ],
  "event_id": "evt_17e5f4dc6888767ed9799f78dfa2cabc"
}
 */
