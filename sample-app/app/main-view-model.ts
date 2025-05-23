import { Observable } from '@nativescript/core'
const coreApplication = require("@nativescript/core/application");
declare const co: any

export class HelloWorldModel extends Observable {
  private _message: string

  private dataDomeSDKWrapper: any

  constructor() {
    super()
    this.updateMessage()

    let androidApp = coreApplication.android.context
    this.dataDomeSDKWrapper = new co.datadome.ns.wrapper.DataDomeSDKWrapper(androidApp, "key", "1.0.0", true)
  }

  get message(): string {
    return this._message
  }

  set message(value: string) {
    if (this._message !== value) {
      this._message = value
      this.notifyPropertyChange('message', value)
    }
  }

  sendRequest() {
    const completionHandler = new co.datadome.ns.wrapper.DataDomeSDKWrapper.CompletionHandler({
        onSuccess(response: string) {
        console.log("✅ Request succeeded:", response)
        this.message = response
      },
        onError(error: string) {
        console.error("❌ Request failed:", error)
      }
    })

    const headers = new java.util.HashMap()
    headers.put("User-Agent", "BLOCKUA")
    headers.put("Accept", "application/json")

    const body = JSON.stringify({
      techno: "Nativescript",
      role: "wrapper"
    });

    this.dataDomeSDKWrapper.makeRequest("post", "https://fastly.datashield.co", headers, body, completionHandler)
  }

  clearCookie() {
    this.dataDomeSDKWrapper.clearDataDomeCookie()
  }

  private updateMessage() {
    this.message = ""
  }
}
