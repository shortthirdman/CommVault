import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '' },
  { path: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true, enableTracing: false })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
