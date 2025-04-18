import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {Title} from "@angular/platform-browser";
import {CommvaultService} from "./services/commvault.service";

@Component({
  selector: 'commvault-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  private appTitle = 'CommVault';
  private service = inject(CommvaultService);

  constructor(private title: Title) {
    this.title.setTitle(this.appTitle);
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }
}
