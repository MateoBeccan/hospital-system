import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IObraSocial } from '@/shared/model/obra-social.model';

import ObraSocialService from './obra-social.service';

export default defineComponent({
  name: 'ObraSocialDetails',
  setup() {
    const obraSocialService = inject('obraSocialService', () => new ObraSocialService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const obraSocial: Ref<IObraSocial> = ref({});

    const retrieveObraSocial = async obraSocialId => {
      try {
        const res = await obraSocialService().find(obraSocialId);
        obraSocial.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.obraSocialId) {
      retrieveObraSocial(route.params.obraSocialId);
    }

    return {
      alertService,
      obraSocial,

      previousState,
    };
  },
});
