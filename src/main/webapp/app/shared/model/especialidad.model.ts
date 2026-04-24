export interface IEspecialidad {
  id?: number;
  codigo?: string;
  nombre?: string;
  descripcion?: string | null;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  activa?: boolean;
}

export class Especialidad implements IEspecialidad {
  constructor(
    public id?: number,
    public codigo?: string,
    public nombre?: string,
    public descripcion?: string | null,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public activa?: boolean,
  ) {
    this.activa = this.activa ?? false;
  }
}
