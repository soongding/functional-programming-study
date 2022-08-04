const fs = require("fs");
const BEST = "best";
const GOOD = "good";

// Action
const fetchSubscribers = () =>
  JSON.parse(fs.readFileSync("subscribers.json", "utf-8"));

// Action
const fetchCoupons = () => JSON.parse(fs.readFileSync("coupons.json", "utf-8"));

// Calc
const subscriberCouponRank = (subscriber) =>
  subscriber.rec_count >= 10 ? BEST : GOOD;

// Calc
const selectCouponsByRank = (coupons, rank) =>
  coupons.filter((coupon) => coupon.rank === rank).map((coupon) => coupon.code);

// Calc
const message = (subscriber, coupons) => {
  return {
    from: "newsletter@coupondog.co",
    to: subscriber.email,
    subject: "Your best weekly coupons inside",
    body: `Here are the best coupons : ${coupons.join(", ")}`,
  };
};

// Calc
const emailForSubscriber = (subscriber, bests, goods) => {
  const rank = subscriberCouponRank(subscriber);

  return rank === BEST
    ? message(subscriber, bests)
    : message(subscriber, goods);
};

// Action
const sendIssue = () => {
  const subscribers = fetchSubscribers();
  const coupons = fetchCoupons();
  const bestCoupons = selectCouponsByRank(coupons, BEST);
  const goodCoupons = selectCouponsByRank(coupons, GOOD);
  const emails = subscribers.map((subscriber) =>
    emailForSubscriber(subscriber, bestCoupons, goodCoupons)
  );

  fs.writeFileSync("sendEmails", JSON.stringify(emails, null, 2));
};

sendIssue();
